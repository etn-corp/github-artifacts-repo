FROM registry.access.redhat.com/ubi8/ubi
# Make a directory for our code and copy it over.
RUN mkdir /TargetAccountPlanner
COPY src/* /TargetAccountPlanner/src

RUN dnf -y install python3-pip && \
      dnf clean all
RUN pip3 install -r /TargetAccountPlanner/requirements.txt && \
      rm -rf /root/.cache
# Set the working directory to where we copied the code.
WORKDIR /TargetAccountPlanner
# Expose port 8000.
EXPOSE 8000
# Run the Flask application via gunicorn.
CMD ["gunicorn", "-b", "0.0.0.0:8000", "TargetAccountPlanner:app"]
